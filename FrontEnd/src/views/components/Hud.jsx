import './Hud.css'
import '../../woodPattern.css'
import woodPattern from '../../assets/pattern/wood.jpg'
import Card from './Card.jsx'

function Hud ({position}){
  const Base = ({pos})=>{
    return (
      <div className='woodPattern object-cover h-20 p-4 overflow-visible'>
        <div className='relative flex'>
          <div className={position == 'bottom'? 'absolute bottom-0 w-full flex justify-center' : 'absolute top-0 w-full flex justify-center'}>
            <Card></Card>
            <Card></Card>
            <Card></Card>
          </div>
        </div>
      </div>
    );
  }

  return (
    <>
      {position == 'bottom' ?
      <div className="absolute bottom-0 w-full">
        <img src={woodPattern} alt="" className='wave-bottom h-40 w-full object-cover'/> 
        <Base pos={position}></Base>
      </div>
      :
      <div className="absolute top-0 w-full">
        <Base pos={position}></Base>
        <img src={woodPattern} alt="" className='wave-top h-40 w-full object-cover'/> 
      </div>
      }
    </>
  );
  
}

export default Hud