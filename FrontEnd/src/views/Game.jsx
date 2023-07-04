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

const apiUrl = 'http://localhost:8080'

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

function Game() {

  const hostNamePlayer = useLocation().state.hostNamePlayer
  const [room, setRoom] = useState(null);
  const [messagesWs, setMessagesWs] = useState([])
  const [gameStarted, setGameStarted] = useState(false);
  const url = window.location.href;
  const roomId = url.split("/").pop();

  let Sock = new Sockjs('http://localhost:8080/ws');
  const stompClient = Stomp.over(Sock)

  const errorAlertFire = () => {
    Swal.fire(errorAlert).then(result=>{
      if (result.isConfirmed) {
        window.history.go(-1);
      }
    })
  }

  const WebSocket = (stompClient) => {

    const onMessageReceived = (message) => {
      const response = JSON.parse(message.body);
      console.log(response);
      if (response.type == "READY") {
        setGameStarted(true)
        getRoomData();
      }
      if (response.type == "ROLE") {
        // is the Client the host, or the guest
      }
      if (response.type == "MESSAGE") {
        setMessagesWs(currentValue => [...currentValue, response.message]);
      }
      if (response.type == "ERROR") {
        errorAlertFire()
        setTimeout(()=> window.history.go(-1),5000)
      }
    }

    const onConnected = () => {
      stompClient.subscribe('/room/'+roomId, onMessageReceived);
      stompClient.send(`/app/room/${roomId}/join`, {},JSON.stringify({suscriberName: hostNamePlayer}))
    }

    const onError = () => {Swal.fire(errorAlert)}

    stompClient.connect({},onConnected, onError);

    
  }

  const getRoomData = () => {
    console.log('Room Id: '+roomId);
    axios.get(apiUrl + '/rooms/' + roomId)
      .then(response => {
        //console.log(response);
        if (response.status != 200) {
          throw new Error()
        }
        if (response.status == 200) {
          setRoom(response.data);
          //WebSocket(stompClient);
        }
        console.log(response.data);
        
      })
      .catch(() => {Swal.fire(errorAlert)})
  }


  const sendMessage = () => {
    stompClient.send(`/app/room/${roomId}/interact`, {}, JSON.stringify({message: "Hola Servidor!"}));
  }


  useEffect(() => {
    //getRoomData();
    WebSocket(stompClient);
    /* setTimeout(() => {
      setGameStarted(true);
    }, 5000); */
  }, []);

  return (
    (gameStarted && room) 
    ?
    <div className="relative min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
      <img id="" src={room.stage.image} alt="" className="absolute object-cover w-full h-full brightness-50"/>
      <Hud position='top'></Hud>
      <Looger sendMessage={sendMessage} messagesWs={messagesWs} />
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
