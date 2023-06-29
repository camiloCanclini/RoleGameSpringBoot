import { useParams } from "react-router-dom";
import "./Game.css";
import { useEffect, useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import Hud from './components/Hud.jsx'

const apiUrl = 'http://localhost:8080'
function Game() {

  /* const { roomId } = useParams();
  const [room, setRoom] = useState(null);

  const getRoomData = (roomId) => {
    
    axios.get("http://localhost:8080/rooms/"+ roomId)
      .then(response => {
        console.log(response);
        setRoom(response);
      })
      .catch((err) => {
        Swal.fire({
          icon: 'Error',
          title: 'Conecction Failed!',
          text: 'Something went wrong!',
          footer: '<a href="/">Back to Home</a>'
        })
      })
  }

  useEffect(() => getRoomData(room.stage.image), []); */

  return (
    <div className="relative min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
      <img id="" src="https://wallpaperset.com/w/full/5/6/7/18604.jpg" alt="" className="absolute object-cover w-full h-full brightness-50"/>
      <Hud position='top'></Hud>
      <Hud position='bottom'></Hud>
    </div>
  );
}

export default Game;
