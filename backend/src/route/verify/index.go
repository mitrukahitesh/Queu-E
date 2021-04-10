package verify

import (
	"encoding/json"
	"net/http"
)

type response struct {
	Code    int    `json:"code"`
	Message string `json:"message"`
}

func sendOtp(w http.ResponseWriter, r *http.Request, email string) {
	err := SendOtp(email)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: err.Error(),
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
		})
	}
}

func verifyOtp(w http.ResponseWriter, r *http.Request, email string, pass string, otp string) {
	err := VerifyOtp(email, pass, otp)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: err.Error(),
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
		})
	}
}

func login(w http.ResponseWriter, r *http.Request, email string, pass string) {
	err := Login(email, pass)
	if err != nil {
		json.NewEncoder(w).Encode(response{
			Code:    400,
			Message: err.Error(),
		})
	} else {
		json.NewEncoder(w).Encode(response{
			Code:    200,
			Message: "OK",
		})
	}
}

func HandleVerifyRequest(w http.ResponseWriter, r *http.Request, function string, email string, pass string, otp string) {
	switch function {
	case "sendOtp":
		sendOtp(w, r, email)
	case "verifyOtp":
		verifyOtp(w, r, email, pass, otp)
	case "login":
		login(w, r, email, pass)
	}
}
