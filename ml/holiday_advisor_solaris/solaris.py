import json

from flask import Flask, render_template, request, redirect, session, jsonify

app = Flask(__name__)

@app.route('/', methods=['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'HEAD', 'OPTIONS'])
def facade():
    return None

if __name__ == "__main__":
    pass
