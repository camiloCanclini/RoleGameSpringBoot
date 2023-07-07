import "./Game.css";
import "./alertWood.css";
import { useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import axios from "axios";
import Swal from "sweetalert2";
import Hud from './components/Hud.jsx'
import Looger from './components/Logger.jsx'
import { RotatingLines } from "react-loader-spinner";
import Sockjs from "sockjs-client/dist/sockjs"
import Stomp from "stompjs";





function Game() {

  /*

  APIURL
  
  */
 
  const apiUrl = 'http://localhost:8080'

  /*
  
  PLAYER NAME
  
  */

  const name = useLocation().state.playerName;

   /*
   
  HOOKS

  */

  const [room, setRoom] = useState(null);
  const [id, setPlayerId] = useState(0);
  const [messagesWs, setMessagesWs] = useState([])
  const [wsConnection, setWsConnection] = useState(false)
  const [gameStarted, setGameStarted] = useState(false);
  const [playerOne, setPlayerOne] = useState(null);
  const [playerTwo, setPlayerTwo] = useState(null);
  const url = window.location.href;
  const roomId = url.split("/").pop();

  /*

  ERROR FUNCTION
  
  */

  const errorAlert = {
    icon: 'error',
    title: 'Conecction Failed!',
    text: 'Something went wrong!',
    confirmButtonText: 'Back To Home',
    focusConfirm: false,
    customClass: {
      popup: "alert",
    }
  }

  const ErrorAlertFire = () => {
    Swal.fire(errorAlert).then(result=>{
      if (result.isConfirmed) {
        window.history.go(-1);
      }
    })
  }
  
  /*

  WEBSOCKET SYSTEM
  
  */
  let Sock = new Sockjs('http://localhost:8080/ws');
  const stompClient = Stomp.over(Sock)

  const connectWebSocket = () => {
    stompClient.connect({},onConnected, onError);
  }
  
  const onConnected = () => {
    stompClient.subscribe('/room/'+roomId, onMessageReceived);
    setWsConnection(true)
    joinUserToRoom();
  }

  const onMessageReceived = (message) => {
    const response = JSON.parse(message.body);
    console.log(response);
    if (response.type == "ROOMREADY") {
      getRoomInfo();
      setTimeout(()=>{
        setGameStarted(true)
      },1000);
    }
    if (response.type == "MOVE") {
      setMessagesWs(currentValue => [...currentValue, response.message]);
    }
    if (response.type == "ERROR") {
      errorAlertFire()
      setTimeout(()=> window.history.go(-1),5000)
    }
  }

  const onError = () => {ErrorAlertFire()}

  const sendMessage = () => {
    stompClient.send(`/app/room/${roomId}/interact`, {}, JSON.stringify({message: "Hola Servidor!"}));
  }

  /*
  
  API CALL FUNCTIONS
  
  */
  const getRoomInfo = () => {
    axios.get(apiUrl + '/rooms/' + roomId)
      .then(response => {
        console.log(response.data);
        if (response.status != 200) {
          throw new Error()
        }
        if (response.status == 200) {
          setRoom(response.data)
        }
      })
      .catch(() => {ErrorAlertFire()})
  }

  const joinUserToRoom = () => {
    axios.put(apiUrl+"/rooms/"+roomId+"/join",{name: name})
    .then((response)=>{
      console.log("ID del jugador: ",response.data);
      setPlayerId(response.data.playerId)
    })
    .catch((e) => {
      console.log("error put");
      ErrorAlertFire()}) 
  }

  /* 
  
  START GAME (SETTING PLAYERS) 
  
  */

  useEffect(() => {
      connectWebSocket();
  }, []);

  

  return (
    (gameStarted && room) 
    ?
    <div className="relative min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
      <img id="" src={room.stage.image} alt="" className="absolute object-cover w-full h-full brightness-50"/>
      <Hud position='top' player={id == 1? room.guestPlayer : room.hostPlayer}></Hud>
      <Looger sendMessage={sendMessage} messagesWs={messagesWs} />
      <Hud position='bottom' player={id == 1? room.hostPlayer : room.guestPlayer}></Hud>
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
