import { useState, useEffect } from "react";
import "./App.css";
import bgImg from "./assets/backgroundImages/menuBackground.jpg";
import RoomBox from "./components/RoomBox";

const getRooms = () => {
  return fetch("http://localhost:8080/rooms")
    .then((response) => response.json())
    .then((data) => data)
    .catch(() => {});
};

function App() {
  const testData = {
    1: {
      background:
        "https://static.vecteezy.com/system/resources/previews/006/889/829/non_2x/empty-silhouette-medieval-background-free-vector.jpg",
      playerHost: "CaballeroCamilo123",
      full: false
    },
    2: {
      background:
        "https://img.freepik.com/premium-vector/silhouette-medieval-background-with-medieval-army_1639-32672.jpg",
      playerHost: "PepeYLosGloblos",
      full: false
    },
    3: {
      background:
        "https://img.freepik.com/premium-vector/silhouette-medieval-background-with-medieval-army_1639-32672.jpg",
      playerHost: "PepeYLosGloblos",
      full: false
    },
    4: {
      background:
        "https://img.freepik.com/premium-vector/silhouette-medieval-background-with-medieval-army_1639-32672.jpg",
      playerHost: "PepeYLosGloblos",
      full: false
    },
    44: {
      background:
        "https://img.freepik.com/premium-vector/silhouette-medieval-background-with-medieval-army_1639-32672.jpg",
      playerHost: "PepeYLosGloblos",
      full: false
    },
    22: {
      background:
        "https://img.freepik.com/premium-vector/silhouette-medieval-background-with-medieval-army_1639-32672.jpg",
      playerHost: "PepeYLosGloblos",
      full: false
    },
  };
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
      <div className="buttons w-full h-1/3 flex flex-col justify-center items-center gap-8 p-5">
        <button
          onClick={() => {
            getRooms().then((data) => setRooms(data));
          }}
          className="text-3xl bg-cyan-600 border-solid border-4  p-4 rounded-full w-30 h-30"
        >
          <i className=" fa-solid fa-rotate" aria-hidden="true"></i>
        </button>
        <button className="bg-cyan-600 border-solid border-4 rounded-full w-1/4 p-4 text-2xl">
          Create Room
        </button>
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
