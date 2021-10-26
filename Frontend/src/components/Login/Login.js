import { React, useState } from "react";

export default function Login() {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = (event) => {
    event.preventDefault();

    let credentials = {
      userName,
      password,
    };

    var formBody = [];

    for (var property in credentials) {
      var encodedKey = encodeURIComponent(property);
      var encodedValue = encodeURIComponent(credentials[property]);
      formBody.push(encodedKey + "=" + encodedValue);
    }
    formBody = formBody.join("&");

    fetch("http://localhost:8082/login", {
      method: "POST",
      headers: {
        Accept: "application/json",
        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
      },
      body: formBody,
    })
      .then((response) => response.json())
      .then((result) => {
        console.log("Success:", result);
      })
      .catch((error) => {
        console.error("Error:", error);
      });
  };
  return (
    <div>
      <form>
        <input
          type="text"
          name="UserName"
          placeholder="Enter User Name"
          onChange={(e) => setUserName(e.target.value)}
        ></input>
        <br></br>
        <input
          type="password"
          name="Password"
          placeholder="Enter Password"
          onChange={(e) => setPassword(e.target.value)}
        ></input>
        <br></br>
        <input type="submit" value="submit" onClick={handleSubmit}></input>
      </form>
    </div>
  );
}
