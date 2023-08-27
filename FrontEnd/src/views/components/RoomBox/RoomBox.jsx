import './RoomBox.css'
import Swal from 'sweetalert2'
import { useNavigate } from 'react-router-dom'
import "../../alertWood.css";

// eslint-disable-next-line react/prop-types
function RoomBox({background, playerHost, roomId}){

  const navigate = useNavigate();

  const entersRoom = (roomId)=>{
    Swal.fire({
      title: 'Enter Your Username',
      input: 'text',
      inputAttributes: {
        autocapitalize: 'off'
      },
      showCancelButton: true,
      confirmButtonText: 'Enter to the Room',
      showLoaderOnConfirm: true,
      customClass: {
        popup: "alert",
      },
      preConfirm: (login) => {
        if (!login) {
          Swal.fire({
            icon: 'warning',
            title: 'Oops...',
            text: 'Complete the fields please!',
            customClass: {
              popup: "alert",
            },
          })
        }else{
          return setTimeout(()=> navigate("/game/"+roomId, {state:{playerName: login}}),1000)
        }
      },
      allowOutsideClick: () => !Swal.isLoading()
    })
    
  }

  return (
  <div onClick={()=>entersRoom(roomId)} id={roomId} className="room relative text-xl w-1/2 h-32 my-4 flex flex-col justify-center border-l-white border-solid border-4 rounded-full overflow-hidden">
    <img src={background} alt="" className="absolute brightness-50"/>
    <div className="mx-10">
      <p>RoomId: {roomId}</p>
      <p>Host Player: {playerHost}</p>
    </div>
  </div>
  );
  
}
export default RoomBox