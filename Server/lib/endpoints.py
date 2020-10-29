from flask import request
from flask_restful import Resource

from lib.database import Database


class User(Resource):
    def get(self, username: str):
        data = {}
        data["username"] = username
        return data, 200


class Project(Resource):
    def get(self, project_id: str):
        data = {}
        data["project_id"] = project_id
        return data, 200

    def post(self):
        data = request.json
        return data, 200