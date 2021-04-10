package token

import (
	"context"
	"queue/src/db"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo/options"
)

func GetTotalToken(id string, name string) (int32, error) {
	var val int32
	obj, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return val, err
	}
	client, err := db.GetMongoClient()
	if err != nil {
		return val, err
	}
	collection := client.Database(db.DB).Collection(db.COUNTER)
	filter := bson.D{primitive.E{Key: "org", Value: obj}}
	res := collection.FindOne(
		context.TODO(),
		filter,
		options.FindOne().SetProjection(bson.D{primitive.E{Key: name, Value: 1}, primitive.E{Key: "_id", Value: 0}}),
	)
	var mp map[string]int32
	err = res.Decode(&mp)
	val = mp[name]
	return val, err
}
