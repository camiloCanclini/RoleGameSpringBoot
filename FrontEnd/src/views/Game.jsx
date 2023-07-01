import "./Game.css";
import "./alertWood.css";
import { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import Hud from './components/Hud.jsx'
import Looger from './components/Logger.jsx'
import { RotatingLines } from "react-loader-spinner";
/* import {over} from 'stompjs';
import SockJS from 'sockjs-client'; */

const apiUrl = 'http://localhost:8080'

const errorAlert = {
  icon: 'error',
  title: 'Conecction Failed!',
  text: 'Something went wrong!',
  footer: '<a href="/">Back to Home</a>',
  customClass: {
    popup: "alert",
  }
}

function Game() {


  const [room, setRoom] = useState(null);
  const [gameStarted, setGameStarted] = useState(false);
  const url = window.location.href;
  const roomId = url.split("/").pop();
  const getRoomData = () => {
    console.log(roomId);
    axios.get(apiUrl + '/rooms/' + roomId)
      .then(response => {
        console.log(response);
        if (response.status != 200) {
          throw new Error()
        }
        console.log(response.data);
        setRoom(response.data);
        
      })
      .catch(() => {Swal.fire(errorAlert)})
  }

  
  /* let stompClient = null
  const socket = new SockJS('/room')
  stompClient = over(socket)
  let Sock = new SockJS('http://localhost:8080/ws');
  stompClient = over(Sock);
  stompClient.connect({},onConnected, onError);

  const onConnected = () => {
    stompClient.subscribe('/room/'+roomId, onMessageReceived);
  }

  const onError = () => {

  } */

  useEffect(() => {
    getRoomData();
    setTimeout(() => {
      setGameStarted(true);
    }, 15000);
  }, []);

  return (
    (gameStarted && room) 
    ?
    <div className="relative min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
      <img id="" src={room.stage.image} alt="" className="absolute object-cover w-full h-full brightness-50"/>
      <Hud position='top'></Hud>
      <Looger></Looger>
      <Hud position='bottom'></Hud>
    </div>
    :
    <div className="flex flex-col w-screen h-screen justify-center items-center gap-8">
      <img id="" src="https://wallpaperaccess.com/full/1886598.jpg" alt="" className="absolute object-cover w-full h-full brightness-50 blur-sm"/>
      <p className="z-10 text-4xl">Waiting Opponent</p>
      <p className="z-10 text-4xl">Room ID: {roomId}</p>
      <RotatingLines
        strokeColor="white"
        strokeWidth="5"
        animationDuration="0.75"
        width="96"
        visible={true}
      />
    </div>
  );
}

export default Game;
