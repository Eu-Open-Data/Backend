import json

from flask import Flask, render_template, request, redirect, session, jsonify

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'HEAD', 'OPTIONS'])
def handle_root():
    return "send request of any type to /help to view endpoints"

@app.route('/help', methods=['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'HEAD', 'OPTIONS'])
def handle_help():
    return ("Help\n"
            "/test (any type) - returns your request back as a JSON response")

@app.route('/test', methods=['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'HEAD', 'OPTIONS'])
def handle_test():
    request_data = {
        "method": request.method,
        "headers": dict(request.headers),
        "args": request.args.to_dict(),
        "form": request.form.to_dict(),
        "json": request.get_json(silent=True)
    }

    # Remove Cookie data from the headers if present
    request_data['headers'].pop('Cookie', None)

    return jsonify(request_data)


if __name__ == "__main__":
    app.run(debug=True)
