const crypto = require('crypto');

function base64UrlEncode(str) {
    return Buffer.from(str)
        .toString('base64')
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=/g, '');
}

const header = {
    "alg": "HS256",
    "typ": "JWT"
};

const payload1 = {
    "sub": "1",
    "iat": Math.floor(Date.now() / 1000),
    "exp": Math.floor(Date.now() / 1000) + 3600
};

const payload2 = {
    "sub": "2", 
    "iat": Math.floor(Date.now() / 1000),
    "exp": Math.floor(Date.now() / 1000) + 3600
};

const encodedHeader = base64UrlEncode(JSON.stringify(header));
const encodedPayload1 = base64UrlEncode(JSON.stringify(payload1));
const encodedPayload2 = base64UrlEncode(JSON.stringify(payload2));

// JwtUtil과 동일한 비밀키 사용
const secretBase64 = "cOGy9rwPeTIImdSXi9sbWE0TQe7ZF+AfH8SF8IByFYY=";
const secret = Buffer.from(secretBase64, 'base64');

const signature1 = crypto
    .createHmac('sha256', secret)
    .update(encodedHeader + '.' + encodedPayload1)
    .digest('base64')
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=/g, '');

const signature2 = crypto
    .createHmac('sha256', secret)
    .update(encodedHeader + '.' + encodedPayload2)
    .digest('base64')
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=/g, '');

const jwt1 = encodedHeader + '.' + encodedPayload1 + '.' + signature1;
const jwt2 = encodedHeader + '.' + encodedPayload2 + '.' + signature2;

console.log('=== JWT 토큰 생성 완료 ===');
console.log('User ID 1 토큰:');
console.log(jwt1);
console.log('');
console.log('User ID 2 토큰:');
console.log(jwt2);