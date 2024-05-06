import React, { Component } from 'react';
import axios from 'axios';
import { setAuthHeader } from '../axios_helper';
import Item from './ItemComponent';

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
        axios.get(`/items?page=${currentPage}&size=16`)
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
                    // Handle unauthorized error
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

        if (loading) {
            return <p>Loading...</p>;
        }

        if (error) {
            return <p>Error: {error}</p>;
        }

        if (selectedItem) {
            return <Item item={selectedItem} />;
        }

        return (
            <div className="container mr-0 ml-0 p-0 mw-100">
                <div className="text-center">
                    <h1 className="text-3xl font-bold mt-8 mb-4">Items</h1>
                </div>
                <div className="flex justify-center">
                    <div>
                        <ul className="grid grid-cols-1 sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-8">
                            {data.map((item, index) => (
                                <li key={item.id} onClick={() => this.handleItemClick(item)}
                                    className="relative cursor-pointer">
                                    <div
                                        className="opacity-90 bg-gray-100 shadow-md hover:shadow-2xl hover:opacity-100 max-h-50 min-w-72">
                                        <div className="flex pr-4">
                                            <img src={item.imageUrl} alt={item.title}
                                                 className="w-36 h-50 object-cover mr-4"/>
                                            <div className="text-wrap flex flex-col">
                                                <h3 className="text-lg font-semibold mt-6 mb-2">{item.title} Grand
                                                    Theft Auto V</h3>
                                                <h3 className="text-sm mb-6">platform: {item.platform}</h3>
                                                <h3 className="text-lg absolute bottom-4">36.25 PLN </h3>
                                            </div>
                                        </div>
                                    </div>
                                </li>
                            ))}
                        </ul>
                        <div className="flex justify-center mt-8">
                            <button className="btn btn-primary mr-4"
                                    onClick={() => this.handlePageChange(currentPage - 1)}
                                    disabled={currentPage === 0}>
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                                     xmlns="http://www.w3.org/2000/svg">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                          d="M15 19l-7-7 7-7"></path>
                                </svg>
                            </button>
                            <button className="btn btn-primary"
                                    onClick={() => this.handlePageChange(currentPage + 1)}
                                    disabled={currentPage === totalPages - 1}>
                                <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24"
                                     xmlns="http://www.w3.org/2000/svg">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2"
                                          d="M9 5l7 7-7 7"></path>
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        );
    }
}

export default ItemsContent;
