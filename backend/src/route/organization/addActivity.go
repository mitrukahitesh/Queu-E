package organization

import (
	"context"
	"queue/src/db"
	"queue/src/model"
	"strconv"
	"time"

	"github.com/jasonlvhit/gocron"
	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
)

func AddActivity(task model.Activity, id string) error {

	obj, err := primitive.ObjectIDFromHex(id)
	if err != nil {
		return err
	}
	client, err := db.GetMongoClient()
	if err != nil {
		return err
	}

	//ADD TO COUNTER
	collection := client.Database(db.DB).Collection(db.COUNTER)
	filter := bson.D{primitive.E{Key: "org", Value: obj}}
	updater := bson.D{primitive.E{Key: "$set", Value: bson.D{primitive.E{Key: task.Name, Value: 0}}}}
	_, err = collection.UpdateOne(context.TODO(), filter, updater)
	if err != nil {
		return err
	}

	//ADD TO CURRENT STATUS
	collection = client.Database(db.DB).Collection(db.CURRENT)
	updater = bson.D{primitive.E{Key: "$set", Value: bson.D{primitive.E{Key: task.Name, Value: 0}}}}
	_, err = collection.UpdateOne(context.TODO(), filter, updater)
	if err != nil {
		return err
	}

	//ADD END TIME
	collection = client.Database(db.DB).Collection(db.END)
	endTime := model.EndTime{
		EndHour: task.EndHour,
		EndMin:  task.EndMin,
	}
	updater = bson.D{primitive.E{Key: "$set", Value: bson.D{primitive.E{Key: task.Name, Value: endTime}}}}
	_, err = collection.UpdateOne(context.TODO(), filter, updater)
	if err != nil {
		return err
	}

	//SET RESET SCHEDULER
	location, err := time.LoadLocation("Asia/Kolkata")
	gocron.ChangeLoc(location)
	gocron.Every(1).Day().At(strconv.Itoa(task.EndHour)+":"+strconv.Itoa(task.EndMin)).Do(reset, obj, task.Name)
	gocron.Start()

	//ADD TO ORG ACTIVITY LIST
	collection = client.Database(db.DB).Collection(db.ORG)
	filter = bson.D{primitive.E{Key: "_id", Value: obj}}
	updater = bson.D{primitive.E{Key: "$push", Value: bson.D{primitive.E{Key: "activities", Value: task}}}}
	_, err = collection.UpdateOne(context.TODO(), filter, updater)
	return err
}

func reset(id primitive.ObjectID, name string) {
	client, err := db.GetMongoClient()
	if err != nil {
		return
	}

	//RESET COUNTER
	collection := client.Database(db.DB).Collection(db.COUNTER)
	filter := bson.D{primitive.E{Key: "org", Value: id}}
	updater := bson.D{primitive.E{Key: "$set", Value: bson.D{primitive.E{Key: name, Value: 0}}}}
	collection.UpdateOne(context.TODO(), filter, updater)

	//RESET CURRENT
	// collection = client.Database(db.DB).Collection(db.CURRENT)
	// collection.UpdateOne(context.TODO(), filter, updater)
}
