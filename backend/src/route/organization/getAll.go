package organization

import (
	"context"
	"queue/src/db"
	"queue/src/model"

	"go.mongodb.org/mongo-driver/bson"
)

func GetAll() ([]model.Organization, error) {
	filter := bson.D{{}}
	orgs := []model.Organization{}
	client, err := db.GetMongoClient()
	if err != nil {
		return orgs, err
	}
	collection := client.Database(db.DB).Collection(db.ORG)
	cursor, err := collection.Find(context.TODO(), filter)
	if err != nil {
		return orgs, err
	}
	for cursor.Next(context.TODO()) {
		var o model.Organization
		err := cursor.Decode(&o)
		if err != nil {
			return orgs, err
		}
		orgs = append(orgs, o)
	}
	defer cursor.Close(context.TODO())
	return orgs, err
}
