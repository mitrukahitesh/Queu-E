package verify

import (
	"context"
	"errors"
	"queue/src/db"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

type email_otp struct {
	Email string `json:"email" bson:"email"`
	Otp   string `json:"otp" bson:"otp"`
}

func VerifyOtp(email string, pass string, otp string) error {
	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}
	collection := client.Database(db.DB).Collection(db.OTP)
	filter := bson.D{primitive.E{Key: "email", Value: email}}
	res := collection.FindOne(context.TODO(), filter)
	var eo email_otp
	err = res.Decode(&eo)
	if err != nil {
		return err
	}
	if eo.Otp != otp {
		return errors.New("INVALID OTP")
	}
	collection = client.Database(db.DB).Collection(db.PASS)
	_, err = collection.DeleteMany(context.TODO(), filter)
	if err != nil {
		return err
	}
	data := bson.D{primitive.E{Key: "email", Value: email}, primitive.E{Key: "pass", Value: pass}}
	_, err = collection.InsertOne(context.TODO(), data)
	return err
}
