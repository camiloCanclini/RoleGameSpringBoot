import { Link , redirect} from 'react-router-dom';
import "./CreateRoom.css";
import "../woodPattern.css";
import bgImg from "../assets/backgroundImages/createRoomBg.jpg";
import { useState } from 'react';
import Swal from 'sweetalert2'
import axios from 'axios';

function CreateRoom() {

  const [imgSelected, setImgSelected] = useState(null);
  const [stageId, setStageId] = useState(null);
  const [hostNamePlayer, sethostNamePlayer] = useState(null);
  //const history = useHistory();

  const selectImage = (event) => {
    const newImgSelected = event.target;
    if (imgSelected!=null){
      imgSelected.classList.remove("selected")
      newImgSelected.classList.add("selected")
    }
    setStageId(newImgSelected.id)
    setImgSelected(newImgSelected);
  }

  const roomCreateRequest = () => {
    if (!hostNamePlayer || !stageId) {
      Swal.fire({
        icon: 'warning',
        title: 'Oops...',
        text: 'Complete the fields please!'
      })
    }else{
      axios.post(
        "http://localhost:8080/rooms/",
        JSON.stringify({
          hostPlayer: hostNamePlayer,
          stageId: stageId
        })
      )
        .then(response => {
          console.log(response);
          redirect("/game/"+response.data.roomId)
        })
        .catch((err) => {
          Swal.fire({
            icon: 'error',
            title: 'failed to create room'
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
            <img id="1" src="https://c4.wallpaperflare.com/wallpaper/376/461/209/digital-art-knight-soldier-flag-wallpaper-preview.jpg" className="object-cover  h-32 w-36 rounded-lg cursor-pointer" alt="" onClick={selectImage}/>
            <img id="2" src="https://wallpaperset.com/w/full/5/6/7/18604.jpg" className="object-cover  h-32 w-36 rounded-lg cursor-pointer" alt="" onClick={selectImage}/>
            <img id="3" src="https://w0.peakpx.com/wallpaper/399/17/HD-wallpaper-enchanting-forest-forest-tree-fantasy-luminos-green.jpg" className="object-cover  h-32 w-36 rounded-lg cursor-pointer" alt="" onClick={selectImage} />
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
