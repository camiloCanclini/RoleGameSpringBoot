import './Hud.css'
import '../../woodPattern.css'
import woodPattern from '../../assets/pattern/wood.jpg'
import Card from './Card.jsx'

function Hud ({position, player}){
  const Base = ()=>{
    return (
      <div className='woodPattern object-cover h-36 overflow-visible'>
          <div className={(position == 'bottom'? 'absolute bottom-0 mb-16 pr-8 justify-end' : 'absolute top-0 mt-16 pl-8 justify-start')+' w-full flex  gap-8'}>
            {
              player.cards.map((item, index) => <Card key={index} card={item}/>)
            }
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