/* eslint-disable react/prop-types */

function Stage ({stageId, stage, selectImage}){

  return (
    <div>
      <img key={stageId} id={stageId} src={stage.image} className="object-cover  h-32 w-36 rounded-lg cursor-pointer" alt="" onClick={selectImage}/>
      <div className='w-full'>
          <ul className='flex justify-center items-center gap-2 w-full text-base'>
            <li><i className="fa-solid fa-fire"></i> {stage.heat}</li>
            <li><i className="fa-solid fa-droplet"></i> {stage.humidity}</li>
            <li><i className="fa-solid fa-wind"></i> {stage.wind}</li>
            <li><i className="fa-solid fa-hat-wizard"></i> {stage.magicBoost}</li>
          </ul>
        </div>
    </div>
  );
  
}

export default Stage