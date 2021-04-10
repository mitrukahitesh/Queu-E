package model

type Activity struct {
	Name      string `json:"name" bson:"name"`
	Day       []int  `json:"day" bson:"day"`
	StartHour int    `json:"starthour" bson:"starthour"`
	StartMin  int    `json:"startmin" bson:"startmin"`
	EndHour   int    `json:"endhour" bson:"endhour"`
	EndMin    int    `json:"endmin" bson:"endmin"`
}

type EndTime struct {
	EndHour int `json:"endhour" bson:"endhour"`
	EndMin  int `json:"endmin" bson:"endmin"`
}
