package verify

import (
	"context"
	"math/rand"
	"net/smtp"
	"queue/src/db"
	"strconv"
	"time"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func SendOtp(email string) error {
	// Sender data.
	from := "hiteshmitruka1@gmail.com"
	password := "startUp9031"

	// Receiver email address.
	to := []string{
		email,
	}

	// smtp server configuration.
	smtpHost := "smtp.gmail.com"
	smtpPort := "587"

	rand.Seed(time.Now().UnixNano())
	random := rand.Intn(999999-111111) + 111111

	// Message.
	message := []byte("Your verification code for Queu-E is " + strconv.Itoa(random))

	// Authentication.
	auth := smtp.PlainAuth("", from, password, smtpHost)

	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}
	collection := client.Database(db.DB).Collection(db.OTP)
	filter := bson.D{primitive.E{Key: "email", Value: email}}
	_, err = collection.DeleteMany(context.TODO(), filter)
	if err != nil {
		return err
	}
	updater := bson.D{primitive.E{Key: "email", Value: email}, primitive.E{Key: "otp", Value: strconv.Itoa(random)}}
	_, err = collection.InsertOne(context.TODO(), updater)
	if err != nil {
		return err
	}
	// Sending email.
	err = smtp.SendMail(smtpHost+":"+smtpPort, auth, from, to, message)
	return err
}
