import { useState, useEffect } from "react";
import { Link } from 'react-router-dom';
import testData from "../testData";
import "./Home.css";
import bgImg from "../assets/backgroundImages/menuBackground.jpg";
import "../woodPattern.css";
import RoomBox from "./components/RoomBox";

const getRooms = () => {
  return fetch("http://localhost:8080/rooms")
    .then((response) => response.json())
    .then((data) => {
      console.log(data);
      return data
    })
    .catch(() => {});
};

function App() {
  
  const [rooms, setRooms] = useState([]);

 /*  useEffect(() => {
    // Llamada a la función getRooms dentro del efecto
    getRooms().then((data) => setRooms(data));
  }, []); */

  useEffect(() => {
    // Llamada a setRooms con testData en otro efecto
    setRooms(testData);
  }, []);

  return (
    <div className="min-h-screen flex flex-col items-center justify-center">
      <img id="bgImg" src={bgImg} alt="" />
      <div className=" buttons w-full h-1/3 flex flex-col justify-center items-center gap-8 p-5">
        <button
          onClick={() => {
            getRooms().then((data) => setRooms(data));
          }}
          className="woodPattern text-3xl  border-solid border-4  p-4 rounded-full w-30 h-30"
        >
          <i className=" fa-solid fa-rotate" aria-hidden="true"></i>
        </button>
        <Link to="/createRoom">
          <button className="woodPattern w-full  border-solid border-4 rounded-full w-1/4 p-6 px-12 text-2xl">
            Create Room
          </button>
        </Link>
      </div>

      <div className="roomsContainer w-full flex flex-col items-center p-4">
        {rooms ? (
          Object.keys(rooms).map((key, index) => {
            if (rooms[key].full == false) {
              return (
              <RoomBox
                key={index}
                roomId={key}
                background={rooms[key].background}
                playerHost={rooms[key].playerHost}
              />
              )
            }
        })
        ) : (
          <h1 className="text-6xl my-5 drop-shadow-md"> No Rooms ! </h1>
        )}
      </div>
    </div>
  );
}

export default App;
