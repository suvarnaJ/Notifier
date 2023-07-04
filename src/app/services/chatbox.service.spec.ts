import { Chatbox } from "./chatbox.service";
describe('Chatbox', () => {
  it('should create an instance', () => {
    expect(new Chatbox("")).toBeTruthy();
  });
});