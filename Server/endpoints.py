from flask_restful import Resource


class User(Resource):
    def get(self, username: str):
        data = {}
        data["username"] = username
        return data, 200
