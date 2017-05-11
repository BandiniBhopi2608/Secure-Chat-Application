# Secure-Chat-Application
End-to-End Secure Chat Application</br>

Domain Name: cryptoninja.me</br>

SSL Lab Test: https://www.ssllabs.com/ssltest/analyze.html?d=cryptoninja.me

Demo Video: https://youtu.be/CcFtFqg6ICQ

An Android based secure chat application implemented with the following features:
1. Two step authentication
2. Challenge – Response authentication 
3. JWT authentication
4. Public key exchange using QR code
5. Encryption using PGP for one – to - one chat 
6. User – to – User authentication using Digital signature

Technologies used to develop secure chat application:
1. PHP  ( server side)
2. Android ( Client side )
3. SSL 
4. LAMP
5. REST API
6. JWT ( JASON WEB TOKEN)
7. RETROFIT 
8. MYSQL

Cryptography Profile:
1. Bcrypt  ( used for hashing password )  
2. PGP Algorithm 
 2.1 2048 –bit RSA Public key 
	AES – 256 for encrypting message.
  Mode of encryption used is CTR 
	HMAC SHA – 256 for generating tag
	RSA  used for encryption of 𝐾_𝑒   𝑎𝑛𝑑 𝐾_𝑖

