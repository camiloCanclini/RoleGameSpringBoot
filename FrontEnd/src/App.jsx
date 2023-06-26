import { useState } from "react";
import "./App.css";
import bgImg from "./assets/backgroundImages/menuBackground.jpg";

const getRooms = () => {
  fetch("http://localhost:8080/rooms")
    .then((response) => response.json())
    .then((data) => data)
    .catch(() => {});
};

function App() {
  const [rooms, setRooms] = useState(getRooms());

  return (
    <div className="">
      <img id="bgImg" src={bgImg} alt="" />
      <div className="buttons w-full h-1/3 flex flex-col justify-center items-center gap-8 p-5">
        <button onClick={() => setRooms(getRooms())} className="text-3xl bg-cyan-600 border-solid border-4  p-4 rounded-full w-30 h-30">
          <i className=" fa-solid fa-rotate" aria-hidden="true"></i>
        </button>
        <button className="bg-cyan-600 border-solid border-4 rounded-full w-1/4 p-4 text-2xl">
          Create Room
        </button>
      </div>
      
      <div className="roomsContainer">{rooms ? rooms : "No hay salas"}</div>
    </div>
  );
}

export default App;
