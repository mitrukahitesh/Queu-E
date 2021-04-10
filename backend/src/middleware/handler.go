package middleware

import (
	"net/http"
	"queue/src/route/organization"
	"queue/src/route/token"
	"queue/src/route/verify"
)

func Handler(w http.ResponseWriter, r *http.Request) {

	w.Header().Add("Content-type", "application-json")

	query := r.URL.Query()
	resource := query.Get("resource")
	function := query.Get("function")
	id := query.Get("id")
	name := query.Get("name")

	switch resource {
	case "org":
		organization.HandleOrgReq(w, r, function, id)
	case "token":
		token.HandleTokenReq(w, r, function, id, name)
	case "verify":
		email := query.Get("email")
		otp := query.Get("otp")
		pass := query.Get("password")
		verify.HandleVerifyRequest(w, r, function, email, pass, otp)
	default:
		w.Write([]byte("Hello, Client!"))
	}

}
