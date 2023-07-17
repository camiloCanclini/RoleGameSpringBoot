import "./Game.css";
import "./alertWood.css";
import { useEffect, useState, useContext } from "react";
import { useLocation } from 'react-router-dom';
import axios from "axios";
import Swal from "sweetalert2";
import Hud from './components/Hud.jsx'
import Logger from './components/Logger.jsx'
import StageInfo from "./components/StageInfo";
import { RotatingLines } from "react-loader-spinner";
import Sockjs from "sockjs-client/dist/sockjs"
import Stomp from "stompjs";
import { GameContext } from "../GameContext";

/*
   
  HOOKS

  */
 

function Game() {

  /*

  APIURL
  
  */
 
  const apiUrl = 'http://localhost:8080'

  /*
  
  PLAYER NAME
  
  */

  const name = useLocation().state.playerName;

  
  const [room, setRoom] = useState(null);
  const [id, setPlayerId] = useState(0);
  const [messagesWs, setMessagesWs] = useState([])
  const [wsConnection, setWsConnection] = useState(false)
  const [gameStarted, setGameStarted] = useState(false);
  const [moveType, setMoveType] = useState(null);
  const [choosingMove, setChoosingMove] = useState(false);
  const [targetCard, settargetCard] = useState(null);
  const [myCard, setMyCard] = useState(null);
  const url = window.location.href;
  const roomId = url.split("/").pop();
  const [showDoneBtn, setShowDoneBtn] = useState(false)
  const [showLoading, setShowLoading] = useState(false)

  

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
      },2000);
    }
    if (response.type == "MOVE") {
      console.log(messagesWs);
      setMessagesWs(messagesWs => [...messagesWs, response.message]);
    }
    if (response.type == "ERROR") {
      errorAlertFire()
      setTimeout(()=> window.history.go(-1),5000)
    }
    if (response.type == "SHIFT") {
      getRoomInfo()
      setShowLoading(false)
    }
  }

  const onError = () => {ErrorAlertFire()}

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

  useEffect(()=>{
    const cards = document.querySelectorAll('.card')
    console.log(cards);
    console.log("CAMBIO EL ESTADO");
    cards.forEach((card) => {
      if (card.getAttribute("card-id") == myCard) {
        card.classList.add("cardSelected"); 
      }
      if (card.getAttribute("card-id") == targetCard && moveType == "HIT") {
        card.classList.add("targetCardSelected", "hitSelected"); 
      }
      if (card.getAttribute("card-id") == targetCard && moveType == "DEFEND") {
        card.classList.add("targetCardSelected", "defendSelected"); 
      }
      if (card.getAttribute("card-id") == targetCard && moveType == "SPELL") {
        card.classList.add("targetCardSelected", "spellSelected"); 
      }
    });
  }, [myCard, targetCard])

  useEffect(() => {
    if (myCard != null && targetCard != null && moveType != null) {
      setShowDoneBtn(true)
    }
  }, [choosingMove]);
  
   /*
  
  CONTEXT FOR HOOKS
  
  */

  const sendMove = () => {
    setShowDoneBtn(false)
    const move = {"roomId": roomId, "idPlayer": id,"targetCard":targetCard,"cardUsed":myCard, "moveType": moveType}
    setShowLoading(true)
    stompClient.send(`/app/room/${roomId}/interact`, {}, JSON.stringify(move));
  }

  const cleanMove = () => {
    const cards = document.querySelectorAll('.card')
    console.log(cards);
    cards.forEach((card) => {
      card.classList.remove('selectingMyCard', 'selectingTargetCard', 'myCard', 'hit', 'defend', 'spell'); // Reemplaza 'clase-a-borrar' con la clase que deseas eliminar
    });
  }
  

  return (
    
      (gameStarted && room) 
      ?
      <GameContext.Provider value={{id, room, moveType, setMoveType, choosingMove, setChoosingMove, targetCard, settargetCard, myCard, setMyCard}}>
        {showLoading?
          <>
            <div className="absolute flex flex-col items-center justify-center z-50 text-6xl mb-8 w-full h-full backdrop-blur-md"> 
              <p className="mb-10">Waiting For {id == 1? room.guestPlayer.namePlayer : room.hostPlayer.namePlayer }</p>
              <RotatingLines
              strokeColor="white"
              strokeWidth="5"
              animationDuration="0.75"
              width="96"
              visible={true}
              />
            </div>
            <div className="z-40 absolute w-full h-full bg-black opacity-80 flex flex-col justify-center items-center ">
            </div>
          </>
          :
          null
        }
        <img src={room.stage.image} alt="" className="absolute z-0 w-full h-full object-cover brightness-50" />
            {
            showDoneBtn ? 
            <div className="absolute top-0 bottom-0 left-0 right-0 flex items-center justify-center">
              <button className="z-50 woodPattern p-10 border-4 rounded-lg text-4xl cursor-pointer transition-all hover:scale-105 hover:brightness-150"
              onClick={sendMove}
              >MAKE MOVEMENT</button>
            </div>
            
            :null
            }
            <div className="relative min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
              {(choosingMove == true)?<div className="bg-black opacity-50 z-40 absolute w-full h-full" onClick={()=>{
                setMyCard(null)
                settargetCard(null)
                setMoveType(null)
                setChoosingMove(false)
                setShowDoneBtn(false)
                cleanMove()
                }}></div>:null}
            {/*  <img id="" src={room.stage.image} alt="" className="absolute object-cover w-full h-full brightness-50"/> */}
              <Hud position='top' cleanMove={cleanMove}></Hud>
              <div className="absolute z-20 top-5 right-5 flex items-start gap-6">
                <StageInfo></StageInfo>
                <Logger messagesWs={messagesWs} />
              </div>
              <Hud position='bottom' cleanMove={cleanMove}></Hud>
            </div>
      </GameContext.Provider>
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
