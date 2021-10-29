import { React, useState, useEffect } from "react";
import "./LoginStyles.css";
import { FaTimes } from "react-icons/fa";

export default function Login(props) {
  const [userName, setUserName] = useState("");
  const [password, setPassword] = useState("");
  const [isEmpty, setIsEmpty] = useState(false);
  const [isError, setIsError] = useState(false);

  const handleCloseBtn = () => {
    setIsError(false);
    setIsEmpty(false);
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setIsEmpty(false);
    setIsError(false);
    if (userName !== "" && password !== "") {
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
      try {
        const data = await fetch("http://localhost:8082/login", {
          method: "POST",
          headers: {
            Accept: "application/json",
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
          },
          body: formBody,
        });
        console.log(data);
        const result = await data.json();
        console.log("Success:", result);
        if (result.hasOwnProperty("username")) {
          props.history.push({ pathname: "/" });
        } else if (result.errorMessage === "bad credentials") {
          setIsError(true);
        }
      } catch (error) {
        console.error("Error:", error);
      }
    } else {
      setIsError(true);
      setIsEmpty(true);
    }
  };

  useEffect(() => {
    let clearIntervalId;
    if (isError === true) {
      clearIntervalId = setTimeout(handleCloseBtn, 4000);
    }
    return () => clearInterval(clearIntervalId);
  }, [isError]);

  return (
    <div className="container">
      <form>
        <p className="form-control title">Sign in</p>
        <input
          type="text"
          name="UserName"
          className="form-control username"
          placeholder="username"
          onChange={(e) => setUserName(e.target.value)}
        ></input>
        <input
          type="password"
          name="Password"
          className="form-control password"
          placeholder="password"
          onChange={(e) => setPassword(e.target.value)}
        ></input>
        <input
          type="submit"
          value="Login"
          className="form-control submit-btn"
          onClick={handleSubmit}
        ></input>
      </form>
      {isError && (
        <div className="form-control error-message">
          <div className="content">
            {isEmpty ? (
              <p>please enter username and password</p>
            ) : (
              <p>Invalid username or password!</p>
            )}
          </div>
          <div className="icon">
            <FaTimes onClick={handleCloseBtn} />
          </div>
        </div>
      )}
    </div>
  );
}
