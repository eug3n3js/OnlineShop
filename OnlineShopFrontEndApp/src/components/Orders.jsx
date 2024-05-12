import Shop from "../api/shopApi";
import '../styles/Orders.css';
import React, {useState, useEffect} from 'react';
import {Link} from 'react-router-dom';

function Orders(){
    const [orders, setOrders] = useState([])
    const [load, setLoad] = useState(true)
    useEffect(() => {async function fetchAllOrders(){
        setLoad(false)
        const orders = await Shop.getAllOrders()
        setOrders(orders)
        setLoad(true)
    } fetchAllOrders()}, [])

    function cntLenght(list){
        if (Array.isArray(list)){
            let y = 0;
            for (let i = 0; i < list.length; i++){
                y++;
            }
            return y
        } else {
            return 0
        }
    }
    return (
            <section className="orderList">
                <Link to="/myOrder"><button>View My Bag</button></Link>
                {!load && <p>LOADING...</p>}
                {load && <ul className="orderColumn">
                        {orders.map((order) => <div className="orderCard"><li key={order.id}><h5>Id: {order.id}</h5><p>time: {order.time}</p><p>Completed?: {order.completed ? 'True' : 'False'}</p><h5>Items: {cntLenght(order.itemIds)}</h5></li></div>)}
                    </ul>}
                {load && orders.length === 0 && <p>No orders</p>}
            </section>
    )
}
export default Orders