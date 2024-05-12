import {useNavigate, useParams} from "react-router-dom";
import '../styles/AddToBag.css';
import Shop from "../api/shopApi";
import { useState, useEffect } from "react";
import Cookies from 'universal-cookie';

function AddToBag(){
    const[load, setLoad] = useState(true)
    const[offers, setOffers] = useState([])
    const params = useParams()
    const navigate = useNavigate()
    const cookies = new Cookies();

    function cntLen(list){
        let len = 0;
        list.map(() => len += 1)
        return len;
    }

    async function getOfflineShop(id){
        const shop = await Shop.getShopById(id)
        return 'Shop: ' + shop.name + ' Address: ' + shop.address;
    }

    useEffect(() => {async function fetchAllProductOffers(){
        setLoad(false)
        const offers = await Shop.getAllOffersForProduct(params.id)
        for (let i = 0; i < cntLen(offers); i++){
            offers[i].shop = await getOfflineShop(offers[i].offlineShopId) 
        }
        setOffers(offers)
        const orderId = cookies.get('order_id')
        if (!(typeof orderId === 'number')){
            const order = await Shop.createOrder();
            cookies.set('order_id', order.id)
            console.log('ORDER CREATED' + order.id)
            
        }
        setLoad(true)
    } fetchAllProductOffers()}, [])
    async function addToOrder(offer_id){
        const order = cookies.get('order_id');
        const updatedOrder = await Shop.addToOrder(order, offer_id);
        navigate('/')
    }
    return (
        <section className="addList">
            {!load && <p>LOADING...</p>}
            {load && <ul className="addColumn">{offers.map((offer) => <div className="addCard"><li key={offer.id}><h5>price: {offer.price}</h5><p>quantity: {offer.quantity}</p><p>{offer.shop}</p></li><button onClick={() => addToOrder(offer.id)}>Add to bag</button></div>)}</ul>}
            {load && offers.length === 0 && <p>No offers for this product</p>}

        </section>
    )

}

export default AddToBag