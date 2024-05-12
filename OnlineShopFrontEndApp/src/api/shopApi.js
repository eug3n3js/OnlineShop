export default class Shop {
    static base_link = 'http://127.0.0.1:8080/rest/api/'

    static async getAllProducts(action) {
        const response = await fetch(this.base_link + 'product?action=' + action).catch();
        const resp = await response.json()
        return resp
    }

    static async getAllOrders() {
        const response = await fetch(this.base_link + 'order').catch();
        const resp = await response.json()
        return resp
    }

    static async getAllShops() {
        const response = await fetch(this.base_link + 'shop').catch();
        const resp = await response.json()
        return resp
    }

    static async getShopById(id) {
        const response = await fetch(this.base_link + 'shop/' + id).catch();
        const resp = await response.json()
        return resp
    }

    static async getProductById(id) {
        const response = await fetch(this.base_link + 'product/' + id).catch();
        const resp = await response.json()
        return resp
    }

    static async getAllOffersForOrder(order_id, action) {
        const response = await fetch(this.base_link + 'order/' + order_id + '/offer?action=' + action).catch();
        const resp = await response.json()
        return resp
    }

    static async getAllOffersForProduct(product_id) {
        const response = await fetch(this.base_link + 'product/' + product_id + '/offer').catch();
        const resp = await response.json()
        return resp
    }

    static async createOrder() {
        const response = await fetch(this.base_link + 'order', {
            method : 'POST',
            headers : {
                'Content-Type': 'application/json',
            },
            body : JSON.stringify({isCompleted : false, itemIds : []})
        }).catch();
        const resp = await response.json()
        return resp
    }

    static async deleteOrder(order_id) {
        const response = await fetch(this.base_link + 'order/' + order_id, {
            method : 'DELETE',
            headers : {
                'Content-Type': 'application/json',
            }}).catch();
    }

    static async executeOrder(order_id) {
        const response = await fetch(this.base_link + 'order/' + order_id + '/offer', {
            method : 'PUT',
            headers : {
                'Content-Type': 'application/json',
            }}).catch();
    }

    static async addToOrder(order_id, offer_id) {
        const response = await fetch(this.base_link + 'order/' + order_id + '/offer', {
            method : 'POST',
            headers : {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(offer_id)
        }).catch();
        const resp = await response.json()
        return resp
    }

    static async deleteFromOrder(order_id, offer_id) {
        const response = await fetch(this.base_link + 'order/' + order_id + '/offer', {
            method : 'DELETE',
            headers : {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(offer_id)
        }).catch();
        const resp = await response.json()
        return resp
    }


}