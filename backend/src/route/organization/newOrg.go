package organization

import (
	"context"
	"queue/src/db"
	"queue/src/model"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func NewOrg(task model.Organization) error {

	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}

	//ADD TO ORG LIST
	collection := client.Database(db.DB).Collection(db.ORG)
	res, err := collection.InsertOne(context.TODO(), task)
	if err != nil {
		return nil
	}

	//ADD TO COUNTER
	collection = client.Database(db.DB).Collection(db.COUNTER)
	obj := bson.D{primitive.E{Key: "org", Value: res.InsertedID}}
	_, err = collection.InsertOne(context.TODO(), obj)
	if err != nil {
		return err
	}

	//ADD TO CURRENT STATUS
	collection = client.Database(db.DB).Collection(db.CURRENT)
	obj = bson.D{primitive.E{Key: "org", Value: res.InsertedID}}
	_, err = collection.InsertOne(context.TODO(), obj)
	if err != nil {
		return err
	}

	//ADD TO END TIME
	collection = client.Database(db.DB).Collection(db.END)
	obj = bson.D{primitive.E{Key: "org", Value: res.InsertedID}}
	_, err = collection.InsertOne(context.TODO(), obj)
	return err
}
