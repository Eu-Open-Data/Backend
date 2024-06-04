# venturas v1.4 (31 may 2024) - a simple hotel search API
# using flask & sqlalchemy - connecting to a postgres database
import re

import flask
from flask import Flask, request
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy.orm import aliased
from sqlalchemy import func, true
from fuzzywuzzy import process
from sklearn.cluster import KMeans
from sqlalchemy.ext.hybrid import hybrid_property
import pandas as pd
import numpy as np
import jwt
import bcrypt

app = Flask(__name__)
app.config['SQLALCHEMY_DATABASE_URI'] = ('postgresql://postgres:assword123@'
                                         'bd-dev.c9y4wsaswav8.us-east-1.rds.amazonaws.com:5432/BDdev')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

jwt_secret = 'D1D3F9B194971E3083F88A9858D69A7885344A2833944F48900861718F48D6BD'
hashing_salt = '0921dij5asj5xz829'

# setup models
db = SQLAlchemy(app)


class User(db.Model):
    __tablename__ = 'users'
    id = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String(255))
    first_name = db.Column(db.String(255))
    last_name = db.Column(db.String(255))
    username = db.Column(db.String(255))
    password = db.Column(db.String(255))
    enabled = db.Column(db.Boolean, default=False)
    locked = db.Column(db.Boolean, default=False)
    is_admin = db.Column(db.Boolean, default=False)

    def to_dict(self):
        return {
            "id": self.id,
            "email": self.email,
            "first_name": self.first_name,
            "last_name": self.last_name,
            "username": self.username,
            "password": self.password,
            "enabled": self.enabled,
            "locked": self.locked,
            "is_admin": self.is_admin
        }


class Hotel(db.Model):
    __tablename__ = 'hotels'
    hotel_id = db.Column(db.Integer, primary_key=True)
    # rating = db.Column(db.Float)
    address = db.Column(db.String(255))
    name = db.Column(db.String(255))
    description = db.Column(db.String(255))
    website_url = db.Column(db.String(255))
    latitude = db.Column(db.Float)
    longitude = db.Column(db.Float)
    city_id = db.Column(db.Integer)
    rating_location = db.Column(db.Float)
    rating_sleep = db.Column(db.Float)
    rating_rooms = db.Column(db.Float)
    rating_service = db.Column(db.Float)
    rating_value = db.Column(db.Float)
    rating_cleanliness = db.Column(db.Float)
    tripadvisor_price_level = db.Column(db.Integer)


    def to_dict(self):
        return {
            "hotel_id": int(self.hotel_id),
            # "rating": self.rating,
            "address": self.address,
            "name": self.name,
            "description": self.description,
            "website_url": self.website_url,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "city_id": int(self.city_id),
            "rating_location": self.rating_location,
            "rating_sleep": self.rating_sleep,
            "rating_rooms": self.rating_rooms,
            "rating_service": self.rating_service,
            "rating_value": self.rating_value,
            "rating_cleanliness": self.rating_cleanliness,
            "tripadvisor_price_level": self.tripadvisor_price_level
        }


class Amenity(db.Model):
    __tablename__ = 'amenities'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False)

    def to_dict(self):
        return {
            "id": self.id,
            "name": self.name
        }


class HotelAmenity(db.Model):
    __tablename__ = 'hotels_amenities'
    hotel_id = db.Column(db.Integer, db.ForeignKey('hotels.hotel_id'), primary_key=True)
    amenity_id = db.Column(db.Integer, db.ForeignKey('amenities.id'), primary_key=True)


class City(db.Model):
    __tablename__ = 'cities'
    id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(255), nullable=False)
    country_code = db.Column(db.String(3), db.ForeignKey('countries.code'), nullable=False)
    latitude = db.Column(db.Float)
    longitude = db.Column(db.Float)
    population = db.Column(db.Integer)

    def to_dict(self):
        return {
            "id": self.id,
            "name": self.name,
            "country_code": self.country_code,
            "latitude": self.latitude,
            "longitude": self.longitude,
            "population": self.population
        }


class Country(db.Model):
    __tablename__ = 'countries'
    code = db.Column(db.String(3), primary_key=True)
    name = db.Column(db.String(255), nullable=False)

    def to_dict(self):
        return {
            "code": self.code,
            "name": self.name
        }


class History(db.Model):
    __tablename__ = 'history'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    location_id = db.Column(db.Integer, db.ForeignKey('hotels.hotel_id'))
    timestamp = db.Column(db.DateTime, default=db.func.current_timestamp())

    def to_dict(self):
        return {
            "id": self.id,
            "user_id": self.user_id,
            "location_id": self.location_id,
            "timestamp": self.timestamp
        }


class SearchHistory(db.Model):
    __tablename__ = 'search_history'
    id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('users.id'))
    search_phrase = db.Column(db.String(255))
    timestamp = db.Column(db.DateTime, default=db.func.current_timestamp())

    def to_dict(self):
        return {
            "id": self.id,
            "user_id": self.user_id,
            "search_phrase": self.search_phrase,
            "timestamp": self.timestamp
        }


with app.app_context():
    db.create_all()

def get_user_id_from_token(token):
    """
    function that decodes the user_id from the token
    :param request: flask request object
    :return: user_id or error response
    """

    user_id = None

    if not token:
        return build_response({"error": "token is required"}), 400

    try:
        user_id = jwt.decode(token, jwt_secret, algorithms=["HS256"])['id']
    except:
        return build_response({"error": "Invalid token"}), 400

    return user_id


def build_response(data):
    """
    function that builds a response with the data and disables CORS policy
    :param data: data to be sent in the response
    :return: response with the data and CORS disabled
    """
    response = flask.jsonify(data)
    response.headers.add('Access-Control-Allow-Origin', '*')
    return response


@app.route('/search', methods=['GET'])
def search_location():
    """
    function that searches for hotels based on the query parameters
    :return: list[dict] - list of hotels that match the query parameters
    """

    # filtrare
    hotels_query = Hotel.query
    max_count = request.args.get("max_count")
    search_phrase = request.args.get("search_phrase")
    amenities = request.args.get("amenities")
    cities = request.args.get("cities")
    countries = request.args.get("countries")
    fuzzy_level = request.args.get("fuzzy_level", type=float)
    min_rating = request.args.get("min_rating", type=float)
    min_rating_location = request.args.get("min_rating_location", type=float)
    min_rating_sleep = request.args.get("min_rating_sleep", type=float)
    min_rating_rooms = request.args.get("min_rating_rooms", type=float)
    min_rating_service = request.args.get("min_rating_service", type=float)
    min_rating_value = request.args.get("min_rating_value", type=float)
    min_rating_cleanliness = request.args.get("min_rating_cleanliness", type=float)

    token = request.args.get('token')
    user_id = get_user_id_from_token(token)

    if user_id and search_phrase:
        search_history = SearchHistory(user_id=user_id, search_phrase=search_phrase,
                                       timestamp=db.func.current_timestamp())
        db.session.add(search_history)
        db.session.commit()

    if fuzzy_level is None:
        fuzzy_level = 75
    if search_phrase:
        all_hotels = [hotel.name for hotel in Hotel.query.all()]
        best_match = process.extractBests(search_phrase, all_hotels, scorer=process.fuzz.partial_ratio,
                                          score_cutoff=fuzzy_level, limit=100)
        best_names = [match[0] for match in best_match]
        hotels_query = hotels_query.filter(Hotel.name.in_(best_names))
    if amenities:
        amenity_list = [amenity.strip() for amenity in amenities.split(',')]
        hotel_alias = aliased(Hotel)
        amenity_alias = aliased(Amenity)
        hotel_amenity_alias = aliased(HotelAmenity)

        subquery = db.session.query(
            hotel_amenity_alias.hotel_id,
            func.count(amenity_alias.id).label('amenity_count')
        ).join(amenity_alias, hotel_amenity_alias.amenity_id == amenity_alias.id
               ).filter(amenity_alias.name.in_(amenity_list)
                        ).group_by(hotel_amenity_alias.hotel_id
                                   ).subquery()
        hotels_query = hotels_query.join(subquery, Hotel.hotel_id == subquery.c.hotel_id
                                         ).filter(subquery.c.amenity_count == len(amenity_list))
    if cities:
        city_list = [city.strip() for city in cities.split(',')]
        city_subquery = db.session.query(City.id).filter(City.name.in_(city_list)).subquery()
        hotels_query = hotels_query.filter(Hotel.city_id.in_(city_subquery))
    if countries:
        country_list = [country.strip() for country in countries.split(',')]
        country_subquery = db.session.query(City.id).join(Country).filter(Country.name.in_(country_list)).subquery()
        hotels_query = hotels_query.filter(Hotel.city_id.in_(country_subquery))
    if min_rating:
        hotels_query = hotels_query.filter(Hotel.rating >= min_rating)
    if min_rating_location:
        hotels_query = hotels_query.filter(Hotel.rating_location >= min_rating_location)
    if min_rating_sleep:
        hotels_query = hotels_query.filter(Hotel.rating_sleep >= min_rating_sleep)
    if min_rating_rooms:
        hotels_query = hotels_query.filter(Hotel.rating_rooms >= min_rating_rooms)
    if min_rating_service:
        hotels_query = hotels_query.filter(Hotel.rating_service >= min_rating_service)
    if min_rating_value:
        hotels_query = hotels_query.filter(Hotel.rating_value >= min_rating_value)
    if min_rating_cleanliness:
        hotels_query = hotels_query.filter(Hotel.rating_cleanliness >= min_rating_cleanliness)
    if max_count:
        max_count = int(max_count)
        hotels_query = hotels_query.limit(max_count)
    hotels = hotels_query.all()

    return build_response([hotel.to_dict() for hotel in hotels])


@app.route('/get_search_history', methods=['GET'])
def get_search_history():
    """
    function that returns the search history of a user
    :return: list[dict] - list of search history of a user
    """
    token = request.args.get('token')
    search_phrase = request.args.get('search_phrase')
    max_count = request.args.get('max_count')

    user_id = get_user_id_from_token(token)

    search_history = SearchHistory.query.filter_by(user_id=user_id)
    sorted_search_history = []

    if search_phrase is not None and search_phrase != "":
        search_history = search_history.filter(SearchHistory.search_phrase.ilike(f"%{search_phrase}%"))
    search_history = search_history.order_by(SearchHistory.timestamp.desc())
    if max_count is not None:
        search_history = search_history.limit(max_count)
    search_history = search_history.all()

    return build_response([history.to_dict() for history in search_history])


@app.route('/get_cities', methods=['GET'])
def get_cities():
    """
    function that returns the cities of a country
    :return: list[dict] - list of cities of a country
    """
    country_name = request.args.get('country')
    if not country_name:
        return build_response({"error": "Country name is required"}), 400
    country = Country.query.filter(Country.name.ilike(country_name)).first()
    if not country:
        return build_response({"error": "country param missing!"}), 404
    cities = City.query.filter_by(country_code=country.code).all()
    city_list = [city.to_dict() for city in cities]
    return build_response(city_list)


@app.route('/get_countries', methods=['GET'])
def get_countries():
    """
    function that returns the countries
    :return: list[dict] - list of countries
    """
    countries = Country.query.all()
    country_list = [country.to_dict() for country in countries]
    return build_response(country_list)


@app.route('/get_amenities', methods=['GET'])
def get_amenities():
    """
    function that returns the amenities
    :return: list[dict] - list of amenities
    """
    amenities = Amenity.query.all()
    amenity_list = [amenity.to_dict() for amenity in amenities]
    return build_response(amenity_list)


@app.route('/get_history', methods=['GET'])
def get_history():
    """
    function that returns the history of a user
    :return: list[dict] - list of history of a user
    """
    token = request.args.get('token')

    user_id = get_user_id_from_token(token)

    history = History.query.filter_by(user_id=user_id).all()
    history_list = [history.to_dict() for history in history]

    return build_response(history_list)


@app.route('/location/<id>', methods=['GET'])
def view_location(id):
    """
    function that returns the details of a location
    :param id: id of the location
    :return: dict - details of the location (including amenities)
    """
    token = request.args.get('token')

    user_id = get_user_id_from_token(token)

    location_id = int(id)
    if not location_id:
        return build_response({"error": "location_id is required"}), 400
    # user_id - id ul user ului care acceseaza locatia
    if user_id:
        history = History(user_id=user_id, location_id=location_id, timestamp=db.func.current_timestamp())
        db.session.add(history)
        db.session.commit()
    hotels_query = Hotel.query.filter(Hotel.hotel_id == location_id)
    hotel = hotels_query.first()
    base_info = hotel.to_dict()

    hotel_alias = aliased(Hotel)
    amenity_alias = aliased(Amenity)
    hotel_amenity_alias = aliased(HotelAmenity)
    subquery = db.session.query(amenity_alias.name).join(hotel_amenity_alias,
                                                         amenity_alias.id == hotel_amenity_alias.amenity_id
                                                         ).filter(
        hotel_amenity_alias.hotel_id == location_id).subquery()
    amenities = db.session.query(subquery.c.name).all()
    base_info['amenities'] = [amenity[0] for amenity in amenities]
    return build_response(base_info)


@app.route('/update_model', methods=['PUT'])
def update_model():
    cluster_count = request.args.get('cluster_count', type=int)
    if not cluster_count:
        cluster_count = 30
    # Fetch all hotels that have none of the ratings as None
    hotels_query = Hotel.query.filter(Hotel.rating_location.isnot(None),
                                      Hotel.rating_sleep.isnot(None),
                                      Hotel.rating_rooms.isnot(None),
                                      Hotel.rating_service.isnot(None),
                                      Hotel.rating_value.isnot(None),
                                      Hotel.rating_cleanliness.isnot(None)).all()
    hotels_as_dict = {hotel.hotel_id: hotel.to_dict() for hotel in hotels_query}

    # Fetch all amenities for all hotels
    amenities_query = db.session.query(
        HotelAmenity.hotel_id,
        Amenity.name
    ).join(
        Amenity, Amenity.id == HotelAmenity.amenity_id
    ).all()

    for hotel_id, amenity in amenities_query:
        if hotel_id in hotels_as_dict:
            if 'amenities' not in hotels_as_dict[hotel_id]:
                hotels_as_dict[hotel_id]['amenities'] = []
            hotels_as_dict[hotel_id]['amenities'].append(amenity)

    # prepare data for clustering
    features = ['latitude', 'longitude', 'rating_location', 'rating_sleep', 'rating_rooms', 'rating_service',
                'rating_value', 'rating_cleanliness']
    data = [[hotel[feature] for feature in features] for hotel in hotels_as_dict.values()]

    # perform K-Means clustering
    kmeans = KMeans(n_clusters=cluster_count)
    kmeans.fit(data)

    for i, hotel in enumerate(hotels_as_dict.values()):
        hotel['cluster'] = kmeans.labels_[i]

    # filter hotels to only include those in the same cluster as the given location
    recommended_hotels = [hotel for hotel in hotels_as_dict.values()]

    for hotel in recommended_hotels:
        for key, value in hotel.items():
            if isinstance(value, np.int32):
                hotel[key] = int(value)

    # serialize recommended_hotels and store in file hotel_clustering.csv
    df = pd.DataFrame(recommended_hotels)
    df.to_csv('hotel_clustering.csv', index=False)

    return build_response({"message": "Model updated"})


@app.route('/recommend/<id>', methods=['GET'])
def recommend(id):
    location_id = int(id)
    if not location_id:
        return build_response({"error": "location_id is required"}), 400
    max_count = request.args.get('max_count', type=int)

    # deserialize recommended_hotels from file hotel_clustering.csv
    df = pd.read_csv('hotel_clustering.csv')
    recommended_hotels = df.to_dict('records')

    # make sure that 'amenities' is of type list, not str
    for hotel in recommended_hotels:
        if isinstance(hotel['amenities'], str):
            hotel['amenities'] = hotel['amenities'].replace('[', '').replace(']', '').replace('\'', '').split(', ')

    # only return the hotels from the same cluster as location_id
    location_cluster = recommended_hotels[location_id]['cluster']
    recommended_hotels = [hotel for hotel in recommended_hotels if hotel['cluster'] == location_cluster]

    if max_count:
        recommended_hotels = recommended_hotels[:max_count]
    return build_response(recommended_hotels)


@app.route('/register', methods=['POST'])
def register():
    """
    function that registers a user
    :return: dict - details of the user
    """
    email = request.args.get('email')
    first_name = request.args.get('first_name')
    last_name = request.args.get('last_name')
    username = request.args.get('username')
    password = request.args.get('password')

    if not email or not first_name or not last_name or not username:
        return build_response({"error": "email, first_name, last_name, and username are required"}), 400

    # check if email is regex of email if not return 400
    if not re.match(r"[^@]+@[^@]+\.[^@]+", email):
        return build_response({"error": "Invalid email"}), 400

    # check if username is already in use
    if User.query.filter_by(username=username).first():
        return build_response({"error": "Username already in use"}), 400

    # check if email is already in use
    if User.query.filter_by(email=email).first():
        return build_response({"error": "Email already in use"}), 400

    # the password must have at least one number in it and also have at least 8 characters
    if not re.match(r"^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$", password):
        return build_response({"error": "Password must have at least one number and have at least 8 characters"}), 400

    # hash password using bcrypt
    password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt()).decode('utf-8')

    user = User(
        email=email,
        first_name=first_name,
        last_name=last_name,
        username=username,
        password=password,
        enabled=False,
        locked=False,
        is_admin=False)
    db.session.add(user)
    db.session.commit()

    # generate JWT token with all information
    payload = {
        "id": user.id,
        "email": user.email,
        "first_name": user.first_name,
        "last_name": user.last_name,
        "username": user.username,
        "password": user.password,
        "enabled": user.enabled,
        "locked": user.locked,
        "is_admin": user.is_admin
    }

    token = jwt.encode(payload, jwt_secret, algorithm="HS256")

    return build_response({"token": token, "user": user.to_dict()})


@app.route('/login', methods=['POST'])
def login():
    username = request.args.get('username')
    password = request.args.get('password')

    hashed_password = bcrypt.hashpw(password.encode('utf-8'), bcrypt.gensalt())

    user = User.query.filter_by(username=username).first()
    print("found user with config: ", user.username, user.password)
    print(type(hashed_password), type(user.password.encode('utf-8')))
    if user and bcrypt.checkpw(hashed_password, user.password.encode('utf-8')):
        return build_response({"error": "Invalid username or password"}), 400
    if not user:
        return build_response({"error": "Invalid username or password"}), 400

    # generate JWT token with all information
    payload = {
        "id": user.id,
        "email": user.email,
        "first_name": user.first_name,
        "last_name": user.last_name,
        "username": user.username,
        "password": user.password,
        "enabled": user.enabled,
        "locked": user.locked,
        "is_admin": user.is_admin
    }

    token = jwt.encode(payload, jwt_secret, algorithm="HS256")

    return build_response({"token": token, "user": user.to_dict()})


if __name__ == "__main__":
    # create the database
    with app.app_context():
        db.create_all()

    # run the app (listen to any ip with 0.0.0.0, open port 5000, enable debug mode)
    app.run(host='0.0.0.0', port=5000, debug=True)