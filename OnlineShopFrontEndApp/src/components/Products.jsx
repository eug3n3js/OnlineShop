import Shop from "../api/shopApi";
import '../styles/Products.css';
import {Link} from 'react-router-dom';
import React, {useState, useEffect} from 'react';

function Products(){
    const [products, setProducts] = useState([])
    const [products1, setProducts1] = useState([])
    const [load, setLoad] = useState(true)
    const [filter, setFilter] = useState("all")
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
    useEffect(() => {async function fetchAllProducts(){
        setLoad(false)
        const products = await Shop.getAllProducts(filter)
        setProducts(products)
        const products1 = await Shop.getAllProducts('available')
        setProducts1(products1)
        setLoad(true)
    } fetchAllProducts()}, [])
    function changeFilterAvailable(){
        setFilter('available')
    }
    function changeFilterAll(){
        setFilter('all')
    }
    return (
            <section className="productList">
                
                {!load && <p>LOADING...</p>}
                {load && <button onClick={() => changeFilterAvailable()}>Show available</button>}
                {load && <button onClick={() => changeFilterAll()}>Show all</button>}
                {load && filter == 'all' && <ul className="productColumn">
                        {products.map((product) => <div className="productCard"><li key={product.id}><Link to={'/addToBag/' + product.id}><h5>name: {product.name}</h5></Link><p>description: {product.description}</p><p>Available in : {cntLenght(product.presenceIds)} shops</p></li></div>)}
                    </ul>}
                {load && filter == 'available' && <ul className="productColumn">
                        {products1.map((product) => <div className="productCard"><li key={product.id}><Link to={'/addToBag/' + product.id}><h5>name: {product.name}</h5></Link><p>description: {product.description}</p><p>Available in : {cntLenght(product.presenceIds)} shops</p></li></div>)}
                    </ul>}
                {filter == 'all' && products.length === 0 && <p>No products</p>}
                {filter == 'available' && products1.length === 0 && <p>No products</p>}

            </section>
    )
}
export default Products