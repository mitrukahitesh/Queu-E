package organization

import (
	"encoding/json"
	"net/http"
	"queue/src/model"
)

type response struct {
	Code          int                  `json:"code"`
	Message       string               `json:"message"`
	Organizations []model.Organization `json:"orgs"`
}

func newOrg(w http.ResponseWriter, r *http.Request) {
	var task model.Organization
	err := json.NewDecoder(r.Body).Decode(&task)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
		return
	}
	err = NewOrg(task)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:          200,
			Message:       "OK",
			Organizations: []model.Organization{},
		})
	}
}

func getAll(w http.ResponseWriter, r *http.Request) {
	tasks, err := GetAll()

	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:          200,
			Message:       "OK",
			Organizations: tasks,
		})
	}
}

func getById(w http.ResponseWriter, r *http.Request, id string) {
	tasks, err := GetById(id)

	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:          200,
			Message:       "OK",
			Organizations: tasks,
		})
	}
}

func addActivity(w http.ResponseWriter, r *http.Request, id string) {
	var activity model.Activity
	err := json.NewDecoder(r.Body).Decode(&activity)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
	}
	err = AddActivity(activity, id)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:          400,
			Message:       "Something went wrong",
			Organizations: []model.Organization{},
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:          200,
			Message:       "OK",
			Organizations: []model.Organization{},
		})
	}
}

func HandleOrgReq(w http.ResponseWriter, r *http.Request, function string, id string) {
	switch function {
	case "NewOrg":
		newOrg(w, r)
	case "GetAll":
		getAll(w, r)
	case "GetById":
		getById(w, r, id)
	case "AddActivity":
		addActivity(w, r, id)
	default:
		getAll(w, r)
	}
}
