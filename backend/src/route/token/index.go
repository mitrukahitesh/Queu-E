package token

import (
	"encoding/json"
	"net/http"
)

type response struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
	Value   int32  `json:"value"`
}

func requestToken(w http.ResponseWriter, r *http.Request, id string, name string) {
	val, err := RequestToken(id, name)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: "Something went wrong",
			Value:   -1,
		})
		return
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
			Value:   val,
		})
	}
}

func getCurrent(w http.ResponseWriter, r *http.Request, id string, name string) {
	val, err := GetCurrent(id, name)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: "Something went wrong",
			Value:   -1,
		})
		return
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
			Value:   val,
		})
	}
}

func clearCurrent(w http.ResponseWriter, r *http.Request, id string, name string) {
	err := ClearCurrent(id, name)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: "Something went wrong",
			Value:   -1,
		})
		return
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
			Value:   0,
		})
	}
}

func getTotalToken(w http.ResponseWriter, r *http.Request, id string, name string) {
	val, err := GetTotalToken(id, name)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: "Something went wrong",
			Value:   -1,
		})
		return
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
			Value:   val,
		})
	}
}

func HandleTokenReq(w http.ResponseWriter, r *http.Request, function string, id string, name string) {
	switch function {
	case "RequestToken":
		requestToken(w, r, id, name)
	case "GetCurrent":
		getCurrent(w, r, id, name)
	case "ClearCurrent":
		clearCurrent(w, r, id, name)
	case "GetTotalToken":
		getTotalToken(w, r, id, name)
	}
}
