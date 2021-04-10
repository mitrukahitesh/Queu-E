package model

import "go.mongodb.org/mongo-driver/bson/primitive"

type Organization struct {
	Id         primitive.ObjectID `json:"_id,omitempty" bson:"_id,omitempty"`
	Name       string             `json:"name" bson:"name"`
	Type       string             `json:"type" bson:"type"`
	Email      string             `json:"email" bson:"email"`
	Uid        string             `json:"uid" bson:"uid"`
	Activities []Activity         `json:"activities" bson:"activities"`
}
