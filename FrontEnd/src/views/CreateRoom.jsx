import { Link, useNavigate } from 'react-router-dom';
import "./CreateRoom.css";
import "./alertWood.css"
import "../woodPattern.css";
import bgImg from "../assets/backgroundImages/createRoomBg.jpg";
import { useEffect, useState } from 'react';
import Swal from 'sweetalert2'
import axios from 'axios';
import Stage from './components/Stage.jsx'

function CreateRoom() {

  const [imgSelected, setImgSelected] = useState(null);
  const [stageSelected, setStageSelected] = useState(null);
  const [hostNamePlayer, sethostNamePlayer] = useState(null);
  const [stages, setStages] = useState(null)
  const navigate = useNavigate();

  const selectImage = (event) => {
    const newImgSelected = event.target;
    if (imgSelected!=null){
      imgSelected.classList.remove("selected")
      newImgSelected.classList.add("selected")
    }
    setStageSelected(newImgSelected.id)
    setImgSelected(newImgSelected);
  }

  const getStages = () => {
    axios.get("http://localhost:8080/stages")
      .then(response => {
        console.log(response.data);
        setStages(response.data)
      })
      .catch(() => setStages(null))
  }

  useEffect(getStages,[])

  const roomCreateRequest = () => {
    if (!hostNamePlayer || stageSelected == null) {
      Swal.fire({
        icon: 'warning',
        title: 'Oops...',
        text: 'Complete the fields please!',
        customClass: {
          popup: "alert",
        },
      })
    }else{
      const requestBody = {
        hostPlayer: hostNamePlayer,
        stageId: parseInt(stageSelected)
      }
      console.log(requestBody);
      axios.post(
        "http://localhost:8080/rooms",
        requestBody
      )
        .then(response => {
          console.log(response);
          setTimeout(()=>navigate("/game/"+response.data),2000)
        })
        .catch((err) => {
          console.log(err)
          Swal.fire({
            icon: 'error',
            title: 'Failed To Create Room !',
            customClass: {
              popup: "alert",
            },
          })
        })
      //history.push("/game/"+)
    }
  }

  return (
    <div className=" min-h-screen flex flex-col items-center justify-center gap-8 text-2xl">
      <img id="bgImg" src={bgImg} alt="" />
      <Link to="/">
        <button className="text-3xl woodPattern border-solid border-4 rounded-full w-24 h-16 transition-all duration-75 hover:scale-105">
        <i className="fa-solid fa-reply"></i>
        </button>
      </Link>
      <div className='inputs h-1/3 flex flex-col gap-2 '>
        <div className=' woodPattern border-solid border-4 p-6 rounded-lg flex gap-x-3 items-center flex-wrap'>
          <label htmlFor="" className='basis-0 grow my-2'>Host Player Name</label>
          <input type="text" className='w-full bg-white text-black h-10 rounded-lg p-4 grow ' required onChange={(event)=>sethostNamePlayer(event.target.value.trim(""))}/>
        </div>
        <div className=' woodPattern border-solid border-4 p-6 rounded-lg flex gap-x-3 items-center flex-wrap'>
          <label htmlFor="" className='basis-0 grow min-w-fit my-2'>Stage</label>
          <div className='flex flex-wrap justify-center gap-2 w-full items-center'>
            {stages != null ? 
            Object.keys(stages).map((key, index) => <Stage key={index} stageId={key} stage={stages[key]} selectImage={selectImage}/>)
            :
            <p>NO STAGES!</p>
          }
          </div>
        </div>
      </div>
      <button className='woodPattern p-4 w-1/3 rounded-full border-4 mx-auto mt-8 cursor-pointer transition-all duration-75 hover:scale-105' onClick={roomCreateRequest}>
        Create Room
      </button>
    </div>
  );
}

export default CreateRoom;
