import Shop from "../api/shopApi";
import '../styles/Shops.css';
import React, {useState, useEffect} from 'react';

function Shops(){
    const [shops, setShops] = useState([])
    const [load, setLoad] = useState(true)
    useEffect(() => {async function fetchAllShops(){
        setLoad(false)
        const shops = await Shop.getAllShops()
        setShops(shops)
        setLoad(true)
    } fetchAllShops()}, [])

    function cntLen(list){
        let len = 0;
        list.map(() => len += 1)
        return len;
    }

    return (
            <section className="shopList">
                {!load && <p>LOADING...</p>}
                {load && <ul className="shopColumn">
                        {shops.map((shop) => <div className="shopCard"><li key={shop.id}><h5>name: {shop.name}</h5><p>address: {shop.address}</p><p>Has :{cntLen(shop.offerIds)} offers</p></li></div>)}
                    </ul>}
                {load && shops.length === 0 && <p>No shops</p>}
            </section>
    )
}
export default Shops