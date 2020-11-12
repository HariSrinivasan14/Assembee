from flask import Flask
from flask_restful import Api


class Pages:
    @staticmethod
    def index():
        return "<h1>Assembee Server</h1>", 200


class Server:
    def __init__(self):
        self.app = Flask("Assembee")
        self.api = Api(self.app)
        self.base_url = "/"
        self.init()

    def init(self):
        try:
            import lib.endpoints as endpoints
            self.app.add_url_rule("/", "index", Pages.index)
            resources = [(endpoints.User,
                          f"{self.base_url}user/<string:username>"),
                         (endpoints.Project,
                          f"{self.base_url}project",
                          f"{self.base_url}project/<string:project_id>"),
                         (endpoints.Projects,
                          f"{self.base_url}projects/<string:user_id>"),
                         (endpoints.Categories,
                          f"{self.base_url}categories")
                         ]
            for resource in resources:
                self.api.add_resource(*resource)
        except Exception as error:
            print(error)
            raise

    def start(self, port: int = None, debug: bool = True):
        try:
            self.app.run(debug=debug, port=port)
        except Exception as error:
            print(error)
            raise
        

server = Server()
app = server.app  # Production WSGI server entry point

if __name__ == "__main__":
    server.start(6969)
