import {useState} from "react";
import axios from 'axios';
import {useSelector, useDispatch} from "react-redux";
import {login, saveJwtToken} from "./store";

export default function Login() {
    const dispatch=useDispatch();
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [message, setMessage] = useState("");
    const jwtToken=useSelector(state=>state.userInfo.jwtToken);

    const handleJoin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/join",
                {username, password},
            );
            setMessage(response.data);
        } catch (error) {
            console.log(error);
            setMessage(error.message)
        }
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post("http://localhost:8080/login",
                new URLSearchParams({username, password})
                );
            const token = response.headers["authorization"];
            // const token=response.data;
            await dispatch(saveJwtToken(token));
            console.log("JWT TOKEN:", token);
            await dispatch(login());
        } catch(error) {
            console.log(error);
            setMessage(error.response.data.message)
        }
    }

    return (
        <>
            <form>

                <input type="text" placeholder="아이디" value={username}
                       onChange={(e) => setUsername(e.target.value)}/>

                <input type="password" placeholder="비밀번호" value={password}
                       onChange={(e) => setPassword(e.target.value)}/>

                <button type="button" name="login" onClick={handleLogin}>로그인</button>
                <button type="button" name="join" onClick={handleJoin}>회원가입</button>

            </form>
            <h2>{message}</h2>
        </>
    )
}