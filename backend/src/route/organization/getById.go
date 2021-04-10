package organization

import (
	"context"
	"queue/src/db"
	"queue/src/model"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func GetById(id string) ([]model.Organization, error) {
	orgs := []model.Organization{}
	obj, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return orgs, err
	}
	filter := bson.D{primitive.E{Key: "_id", Value: obj}}
	client, err := db.GetMongoClient()
	if err != nil {
		return orgs, err
	}
	collection := client.Database(db.DB).Collection(db.ORG)
	res := collection.FindOne(context.TODO(), filter)
	var o model.Organization
	res.Decode(&o)
	orgs = append(orgs, o)
	return orgs, err
}
