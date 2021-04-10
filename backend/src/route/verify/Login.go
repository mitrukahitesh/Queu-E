package verify

import (
	"context"
	"errors"
	"queue/src/db"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func Login(email string, pass string) error {
	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}
	filter := bson.D{primitive.E{Key: "email", Value: email}}
	collection := client.Database(db.DB).Collection(db.PASS)
	res := collection.FindOne(context.TODO(), filter)
	var mp map[string]string
	err = res.Decode(&mp)
	if err != nil {
		return err
	}
	if pass != mp["pass"] {
		return errors.New("INVALID PASSWORD")
	}
	return nil
}
