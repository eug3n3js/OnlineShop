import Shop from "../api/shopApi";
import '../styles/MyOrder.css';
import React, {useState, useEffect} from 'react';
import Cookies from 'universal-cookie';

function MyOrder(){
    const cookies = new Cookies();
    // cookies.set('order_id', undefined)
    const orderId = cookies.get('order_id')
    const [offers, setOffers] = useState([])
    const [load, setLoad] = useState(true)
    const [exception, setException] = useState(false)
    const [finished, setFinished] = useState(false)
    const [finishedId, setFinishedId] = useState(undefined)

    function cntSum(list){
        let sum = 0;
        list.map((offer) => sum += offer.price)
        return sum;
    }

    function cntLen(list){
        let len = 0;
        list.map(() => len += 1)
        return len;
    }

    async function getProduct(id){
        const product = await Shop.getProductById(id)
        return 'Product: ' + product.name + ' Description: ' + product.description;
    }

    async function getOfflineShop(id){
        const shop = await Shop.getShopById(id)
        return 'Shop: ' + shop.name + ' Address: ' + shop.address;
    }

    useEffect(() => {async function fetchAllOffers(){
        if (typeof orderId == 'number'){
            setLoad(false)
            let offers1 = await Shop.getAllOffersForOrder(orderId, 'all')
            if (cntSum(offers1) < 1){
                console.log('deleting')
                await Shop.deleteOrder(orderId)
                cookies.set('order_id', undefined)
                setLoad(true)
                return;
            }
            for (let i = 0; i < cntLen(offers1); i++){
                offers1[i].product = await getProduct(offers1[i].productId) 
                console.log(offers1[i].product);
                offers1[i].shop = await getOfflineShop(offers1[i].offlineShopId) 
                console.log(offers1[i].shop);
            }
            setOffers(offers1)
            setLoad(true)
        }
    } fetchAllOffers()}, [])

    async function executeOrder(){
        const offers1 = await Shop.getAllOffersForOrder(orderId, 'unavailable')
        if (cntSum(offers1) > 0){
            setException(true)
            return;
        }
        await Shop.executeOrder(orderId)
        setFinished(true)
        setFinishedId(orderId)
        cookies.set('order_id', undefined)
    }

    async function deleteFromBag(offer_id){
        await Shop.deleteFromOrder(orderId, offer_id)
        window.location.reload();
    }


    return (
        <section className="offerList">
            {!finished && typeof orderId != 'number' && <p>YOU DONT HAVE ANY ORDER FOR NOW.
                CREATE IT WITH ADDING SOMETHING TO BAG</p>}
            {!finished && typeof orderId == 'number' && !load && <p>loading..</p>}
            {!finished && typeof orderId == 'number' && load && cntSum(offers) == 0 && <p>BAG IS EMPTY</p>}
            {!finished && typeof orderId == 'number' && load && cntSum(offers) != 0 && <h4>SUM : {cntSum(offers)}</h4>}
            {!finished && typeof orderId == 'number' && load && <ul className="offerColumn">{offers.map((offer) => <div className="productCard"><li key={offer.id}><h5>price: {offer.price}</h5><p>quantity: {offer.quantity}</p><p>{offer.product}</p><p>{offer.shop}</p></li><button onClick={() => deleteFromBag(offer.id)}>Delete from bag</button></div>)}</ul>}
            {!finished && typeof orderId == 'number' && load && <button onClick={() => executeOrder()}>Finish Order</button>}  
            {!finished && typeof orderId == 'number' && exception && <p>Your order contains offers with 0 quantity. Please, remove it from bag and try again</p>}
            {finished && <p>Your Order Is Completed. Here is its unique id: {finishedId}</p>}
            {finished && <ul className="offerColumn">{offers.map((offer) => <div className="offerCard"><li key={offer.id}><h5>price: {offer.price}</h5><p>quantity: {offer.quantity}</p><p>{offer.product}</p><p>{offer.shop}</p></li></div>)}</ul>}

        </section>
    )
}
export default MyOrder