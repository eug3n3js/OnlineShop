import Navbar from './components/NavBar'
import Products from './components/Products'
import Orders from './components/Orders'
import MyOrder from './components/MyOrder'
import AddToBag from './components/AddToBag'
import Shops from './components/Shops'
import Home from './components/Home'
import { BrowserRouter, Route , Routes } from 'react-router-dom'

function App() {

  return (
    <BrowserRouter>
      <Navbar/>
      <Routes>
        <Route path="/" element={<Home/>}/>
        <Route path="/products" element ={<Products/>}/>
        <Route path="/shops" element ={<Shops/>}/>
        <Route path="/orders" element ={<Orders/>}/>
        <Route path="/myOrder" element ={<MyOrder/>}/>
        <Route path="/addToBag/:id" element ={<AddToBag/>}/>
      </Routes>
    </BrowserRouter>
    
  )
}

export default App
