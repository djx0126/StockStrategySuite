var webpack = require('webpack');
module.exports = {
    entry: [
        './app.js'
    ],
    output: {
        path: './target',
        filename: 'server.js'
    },
    module: {
        loaders: [
            // { test: /\.js?$/, loaders: ['react-hot', 'babel'], exclude:     /node_modules/ },
            { test: /\.js$/, exclude: /node_modules/, loader: 'babel-loader', include: [
                "app",
                "routes"
            ]}
        ]
    },
    resolve:{
        extensions:['.js','.json']
    },
    node: {
        fs: "empty",
        net: "empty"
    },
    plugins: [
        new webpack.NoEmitOnErrorsPlugin()
    ]
};