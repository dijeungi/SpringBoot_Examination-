import {useState} from "react";
import {useDispatch, useSelector} from "react-redux";
import axios from "axios";
import {logout, saveJwtToken} from "./store";

export default function TestConponents() {
    const [message, setMessage]=useState("");
    const dispatch = useDispatch();
    const jwtToken=useSelector(state=>state.userInfo.jwtToken);


    const handleLogout=async (e)=>{
        await dispatch(logout());
        console.log(jwtToken);
        setMessage("로그아웃 되었습니다.");
    };
    const handleAdminClick=async (e)=>{
        e.preventDefault();
        try{
            const response = await axios.get("http://localhost:8080/admin", {
                headers: {
                    Authorization: jwtToken,
                },
            });

            setMessage(response.data);

        }catch(error){
            if(error.response.status === 401) {
                setMessage(error.response.data.message);
            } else if (error.response.status === 403) {
                setMessage(error.response.data);
            } else {
                setMessage(error.message);
                console.log(error);
            }

        }

    }
    return (
        <>
            <button onClick={handleLogout}>LOGOUT</button>
            <button onClick={handleAdminClick}>ADMIN</button>
            <h1>{message}</h1>
        </>
    )
}