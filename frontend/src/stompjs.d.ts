declare module 'stompjs' {
  export interface Frame {
    command: string;
    headers: any;
    body: string;
  }

  export interface Client {
    connect(headers: any, connectCallback: (frame: Frame) => void, errorCallback?: (error: any) => void): void;
    disconnect(disconnectCallback?: () => void): void;
    send(destination: string, headers: any, body: string): void;
    subscribe(destination: string, callback: (message: any) => void, headers?: any): any;
    unsubscribe(id: string): void;
    begin(transaction: string): void;
    commit(transaction: string): void;
    abort(transaction: string): void;
    ack(messageId: string, subscription: string, headers?: any): void;
    nack(messageId: string, subscription: string, headers?: any): void;
    connected: boolean;
  }

  export function over(ws: any): Client;
}

declare module 'sockjs-client' {
  class SockJS {
    constructor(url: string, protocols?: string | string[], options?: any);
    send(data: string): void;
    close(): void;
    onopen: (() => void) | null;
    onmessage: ((event: any) => void) | null;
    onerror: ((event: any) => void) | null;
    onclose: (() => void) | null;
  }
  export default SockJS;
}
