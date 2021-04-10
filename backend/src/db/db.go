package db

import (
	"context"
	"os"
	"sync"

	"go.mongodb.org/mongo-driver/mongo"
	"go.mongodb.org/mongo-driver/mongo/options"
)

var mongoClient *mongo.Client
var err error
var mongoOnce sync.Once

const (
	DB      = "queue"
	ORG     = "organizations"
	COUNTER = "counter"
	CURRENT = "current"
	END     = "endtime"
)

func GetMongoClient() (*mongo.Client, error) {
	//SET DB URI
	connString := os.Getenv("MONGO_URI")
	if connString == "" {
		connString = "mongodb://localhost:27017"
	}
	//SET MONGO CLIENT
	mongoOnce.Do(func() {
		clientOptions := options.Client().ApplyURI(connString)
		mongoClient, err = mongo.Connect(context.TODO(), clientOptions)
		if err != nil {
			mongoClient = nil
			return
		}
		err = mongoClient.Ping(context.TODO(), nil)
	})
	return mongoClient, err
}
