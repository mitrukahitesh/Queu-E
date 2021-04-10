package main

import (
	"fmt"
	"net/http"
	"os"
	"queue/src/db"
	"queue/src/middleware"
)

func main() {
	//CONNECT MONGODB
	_, err := db.GetMongoClient()
	if err != nil {
		fmt.Println("[ERROR] Cannot connect to database...")
		return
	}

	//CONNECT FIREBASE

	//STARTING SERVER
	mux := http.NewServeMux()
	mux.HandleFunc("/", middleware.Handler)
	fmt.Println("[STARTED] Server started...")
	port := os.Getenv("PORT")
	if port == "" {
		port = "8080"
	}
	err = http.ListenAndServe(":"+port, mux)
	if err != nil {
		fmt.Println("[ERROR]", err)
		return
	}
}
