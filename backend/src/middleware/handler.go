package middleware

import (
	"net/http"
	"queue/src/route/organization"
	"queue/src/route/token"
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
	default:
		w.Write([]byte("Hello, Client!"))
	}

}
