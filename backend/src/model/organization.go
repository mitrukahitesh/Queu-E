package model

type Organization struct {
	Id         string     `json:"_id" bson:"_id"`
	Name       string     `json:"name" bson:"name"`
	Type       string     `json:"type" bson:"type"`
	Email      string     `json:"email" bson:"email"`
	Uid        string     `json:"uid" bson:"uid"`
	Activities []Activity `json:"activities" bson:"activities"`
}
