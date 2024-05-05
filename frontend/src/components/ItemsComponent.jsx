import React, { Component } from 'react';
import axios from 'axios';
import { setAuthHeader } from '../axios_helper';
import Item from './ItemComponent'; // Import the Item component

class ItemsContent extends Component {
    constructor(props) {
        super(props);
        this.state = {
            data: [],
            selectedItem: null,
            error: null,
            loading: true,
            currentPage: 0,
            totalPages: 1,
        };
    }

    componentDidMount() {
        this.fetchData();
    }

    fetchData = () => {
        const { currentPage } = this.state;
        axios.get(`/items?page=${currentPage}&size=12`)
            .then(response => {
                this.setState({
                    data: response.data.content,
                    loading: false,
                    currentPage: response.data.number,
                    totalPages: response.data.totalPages,
                    error: null
                });
            })
            .catch(error => {
                if (error.response && error.response.status === 401) {
                }
                this.setState({ error: error.message, loading: false });
            });
    }

    handlePageChange = (pageNumber) => {
        this.setState({ currentPage: pageNumber, loading: true }, () => {
            this.fetchData();
        });
    }

    handleItemClick = (item) => {
        this.setState({ selectedItem: item });
    }

    render() {
        const { data, loading, error, selectedItem, currentPage, totalPages } = this.state;

        return (
            <div className="container mr-0 ml-0 p-0 mw-100">
                <div className="text-center">
                    <h1 className="text-3xl font-bold mt-8 mb-4">Items</h1>
                </div>
                <div className="flex justify-center">
                    {loading ? (
                        <p>Loading...</p>
                    ) : error ? (
                        <p>Error: {error}</p>
                    ) : selectedItem ? (
                        <Item item={selectedItem} />
                    ) : (
                        <div>
                            <ul className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-9">
                                {data.map((item, index) => (
                                    <li key={item.id} onClick={() => this.handleItemClick(item)} style={{ cursor: 'pointer', minWidth: '450px'}}>
                                        <div className="opacity-90 bg-gray-100 border-1 hover:border-gray-500 hover:opacity-100">
                                            <div className="flex items-center justify-between">
                                                <div className="flex">
                                                    <img src={item.imageUrl} alt={item.title} className="w-40 h-56 object-cover mr-4"/>
                                                    <div>
                                                        <h3 className="text-lg font-semibold mb-2 mt-6">{item.title}</h3>
                                                        <h3 className="text-sm mb-2 mt-6" >platform: {item.platform}</h3>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                ))}
                            </ul>
                            <div className="flex justify-center mt-8">
                                <button className="btn btn-primary mr-4" onClick={() => this.handlePageChange(currentPage - 1)} disabled={currentPage === 0}>
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M15 19l-7-7 7-7"></path>
                                    </svg>
                                </button>
                                <button className="btn btn-primary" onClick={() => this.handlePageChange(currentPage + 1)} disabled={currentPage === totalPages - 1}>
                                    <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M9 5l7 7-7 7"></path>
                                    </svg>
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        );
    }
}

export default ItemsContent;
