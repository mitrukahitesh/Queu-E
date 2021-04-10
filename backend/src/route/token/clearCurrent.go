package token

import (
	"context"
	"queue/src/db"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func ClearCurrent(id string, name string) error {
	obj, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}
	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}
	collection := client.Database(db.DB).Collection(db.CURRENT)
	filter := bson.D{primitive.E{Key: "org", Value: obj}}
	updater := bson.D{primitive.E{Key: "$set", Value: bson.D{primitive.E{Key: name, Value: 0}}}}
	_, err = collection.UpdateOne(context.TODO(), filter, updater)
	return err
}
